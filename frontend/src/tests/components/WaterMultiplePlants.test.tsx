import { describe, it, expect, vi, beforeEach } from "vitest";
import { Toaster, toast } from "react-hot-toast";

vi.mock("../../context/GardenContext", () => ({
  useGarden: vi.fn(),
}));

import { http, HttpResponse } from "msw";
import { server } from "../../mocks/server";
import { TEST_API_BASE_URL } from "../../mocks/config";
import { fireEvent, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { WaterMultiplePlantsModal } from "../../components/WaterMultiplePlantsModal";
import { AuthProvider } from "../../context/AuthContext";
import {
  mockGardenContextEmpty,
  mockGardenContextWithPlants,
} from "../contexts/mockGardenContext";
import { mockUserPlants } from "../../mocks/mockUserPlantsData";
import { useGarden } from "../../context/GardenContext";

// setup
const renderWaterMultiplePlantModal = () => {
  const mockOnClose = vi.fn();

  const renderResult = render(
    <MemoryRouter>
      <AuthProvider>
        <Toaster />
        <WaterMultiplePlantsModal onClose={mockOnClose} />
      </AuthProvider>
    </MemoryRouter>,
  );

  return { ...renderResult, mockOnClose };
};

// correct rendering of the component
describe("WaterMultipleModal rendering", () => {
  it("should not display checkboxes when garden is empty", () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextEmpty);

    renderWaterMultiplePlantModal();

    const checkboxes = screen.queryAllByRole("checkbox");
    expect(checkboxes).toHaveLength(0);
    expect(
      screen.getByText(/You have no plant to water!/i),
    ).toBeInTheDocument();
  });

  it("should display checkboxes when garden have plants", () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextWithPlants);

    renderWaterMultiplePlantModal();

    const checkboxes = screen.queryAllByRole("checkbox");
    expect(checkboxes).toHaveLength(mockUserPlants.length);
  });
  it("should display the empty message", () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextEmpty);

    renderWaterMultiplePlantModal();

    expect(
      screen.getByText(/You have no plant to water!/i),
    ).toBeInTheDocument();
  });
});

describe("Checkbox interactions water multiple plants", () => {
  it("should check the checkbox", async () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextWithPlants);
    const user = userEvent.setup();

    renderWaterMultiplePlantModal();

    const checkboxes = screen.getAllByRole("checkbox");

    await user.click(checkboxes[0]);

    expect(checkboxes[0]).toBeChecked();
    expect(
      screen.getByRole("button", { name: /Water 1 plant/i }),
    ).toBeEnabled();
  });

  it("should uncheck the checkbox", async () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextWithPlants);
    const user = userEvent.setup();

    renderWaterMultiplePlantModal();

    const checkboxes = screen.getAllByRole("checkbox");

    await user.click(checkboxes[0]);
    expect(checkboxes[0]).toBeChecked();

    await user.click(checkboxes[0]);

    expect(checkboxes[0]).not.toBeChecked();
    expect(
      screen.getByRole("button", { name: /Water your plants/i }),
    ).toBeDisabled();
  });

  it("should check all the checkboxes when clinking select all", async () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextWithPlants);
    const user = userEvent.setup();

    renderWaterMultiplePlantModal();

    const selectAllButton = screen.getByRole("button", { name: /select all/i });
    await user.click(selectAllButton);

    const checkboxes = screen.getAllByRole("checkbox");

    checkboxes.forEach((checkbox) => {
      expect(checkbox).toBeChecked();
    });

    expect(
      screen.getByRole("button", { name: /Deselect all/i }),
    ).toBeInTheDocument();
    expect(
      screen.getByRole("button", {
        name: `Water ${mockUserPlants.length} plants`,
      }),
    );
  });

  it("should uncheck all the checkboxes when clicking deselect all", async () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextWithPlants);
    const user = userEvent.setup();

    renderWaterMultiplePlantModal();

    const selectAllButton = screen.getByRole("button", { name: /select all/i });
    await user.click(selectAllButton);

    const checkboxes = screen.getAllByRole("checkbox");

    checkboxes.forEach((checkbox) => {
      expect(checkbox).toBeChecked();
    });
    await user.click(selectAllButton);

    checkboxes.forEach((checkbox) => {
      expect(checkbox).not.toBeChecked();
    });
    expect(
      screen.getByRole("button", { name: /Select all/i }),
    ).toBeInTheDocument();
    expect(
      screen.getByRole("button", {
        name: /Water your plants/i,
      }),
    );
  });
});

describe("Watering plants API call - Error", () => {
  beforeEach(() => {
    vi.clearAllMocks();

    vi.mocked(useGarden).mockReturnValue({
      plants: mockUserPlants,
      refreshGarden: vi.fn(),
      isLoading: false,
    });
    server.use(
      http.patch(`${TEST_API_BASE_URL}/api/user-plants/water-multiples`, () => {
        return HttpResponse.json(
          { message: "Internal server Error" },
          { status: 500 },
        );
      }),
    );
  });

  it("should handle the API error", async () => {
    const user = userEvent.setup();
    const { mockOnClose } = renderWaterMultiplePlantModal();

    const { refreshGarden } = vi.mocked(useGarden).mock.results[0].value;

    const checkboxes = screen.getAllByRole("checkbox");
    await user.click(checkboxes[0]);
    await user.click(checkboxes[1]);

    const waterButton = screen.getByRole("button", { name: /Water 2 plants/i });
    await user.click(waterButton);

    await new Promise((resolve) => setTimeout(resolve, 500));

    expect(refreshGarden).not.toHaveBeenCalled();
    expect(mockOnClose).not.toHaveBeenCalled();
  });
});

describe("Watering multiple API call - Success", () => {
  beforeEach(() => {
    vi.clearAllMocks();

    vi.mocked(useGarden).mockReturnValue({
      plants: mockUserPlants,
      refreshGarden: vi.fn(),
      isLoading: false,
    });
  });

  it("should water the selected plant successfully", async () => {
    const user = userEvent.setup();

    const { mockOnClose } = renderWaterMultiplePlantModal();

    const checkboxes = screen.getAllByRole("checkbox");
    await user.click(checkboxes[0]);
    await user.click(checkboxes[1]);

    const waterButton = screen.getByRole("button", { name: /Water 2 plants/i });
    await user.click(waterButton);

    const { refreshGarden } = vi.mocked(useGarden).mock.results[0].value;
    expect(refreshGarden).toHaveBeenCalled();

    await waitFor(
      () => {
        expect(mockOnClose).toHaveBeenCalled();
      },
      { timeout: 3000 },
    );
  });
});

describe("Date input interaction", () => {
  it("should allow changing the watering date", async () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextWithPlants);

    renderWaterMultiplePlantModal();

    const dateInput = screen.getByLabelText(
      /Watering date/i,
    ) as HTMLInputElement;

    fireEvent.change(dateInput, { target: { value: "2025-01-15" } });

    expect(dateInput).toHaveValue("2025-01-15");
  });
  it("should not allow future dates", async () => {
    vi.mocked(useGarden).mockReturnValue(mockGardenContextWithPlants);

    renderWaterMultiplePlantModal();

    const dateInput = screen.getByLabelText(
      /Watering date/i,
    ) as HTMLInputElement;

    const today = new Date().toISOString().split("T")[0];
    expect(dateInput.max).toBe(today);
  });
});

describe("Loading state during submission", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(useGarden).mockReturnValue({
      plants: mockUserPlants,
      refreshGarden: vi.fn(),
      isLoading: false,
    });
    server.use(
      http.patch(
        `${TEST_API_BASE_URL}/api/user-plants/water-multiples`,
        async () => {
          await new Promise((resolve) => setTimeout(resolve, 1000));
          return HttpResponse.json({ message: "Success" }, { status: 200 });
        },
      ),
    );
  });

  it("should show 'Watering...' text and disable button while submitting", async () => {
    const user = userEvent.setup();

    renderWaterMultiplePlantModal();

    const checkboxes = screen.getAllByRole("checkbox");
    await user.click(checkboxes[0]);

    const waterButton = screen.getByRole("button", { name: /Water 1 plant/i });

    await user.click(waterButton);

    screen.debug();

    await waitFor(
      () => {
        expect(
          screen.getByRole("button", { name: /Watering/i }),
        ).toBeInTheDocument();
      },
      { timeout: 1000 },
    );
    const submittingButton = screen.getByRole("button", { name: /Watering/i });
    expect(submittingButton).toHaveAttribute("aria-disabled", "true");
  });
});
