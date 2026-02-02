import { vi } from "vitest";
import { mockUserPlants } from "../../mocks/mockUserPlantsData";
import type { GardenContextType } from "../../types/GardenContextType";

export const mockGardenContextWithPlants: GardenContextType = {
  plants: mockUserPlants,
  refreshGarden: vi.fn(),
  isLoading: false,
};

export const mockGardenContextEmpty: GardenContextType = {
  plants: [],
  refreshGarden: vi.fn(),
  isLoading: false,
};

export const mockGardenContextLoading: GardenContextType = {
  plants: [],
  refreshGarden: vi.fn(),
  isLoading: true,
};
