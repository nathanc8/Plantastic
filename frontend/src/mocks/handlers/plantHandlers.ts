import { http, HttpResponse } from "msw";
import { TEST_API_BASE_URL } from "../config";
import { mockPlants } from "../mockPlantData";

export const plantSummariesHandlers = [
  http.get(`${TEST_API_BASE_URL}/api/plants/summaries`, () => {
    return HttpResponse.json(mockPlants, { status: 200 });
  }),
];

export const wateringMultiplePlantsHandlers = [
  http.patch(
    `${TEST_API_BASE_URL}/api/user-plants/water-multiples`,
    async ({ request }) => {
      const url = new URL(request.url);
      const date = url.searchParams.get("date");

      const plantIds = (await request.json()) as number[];

      return HttpResponse.json(
        {
          message: "Your plants are no longer thirsty",
          plantIds,
          date,
        },
        { status: 200 },
      );
    },
  ),
];
