import {
  loginHandlers,
  logoutHandlers,
  signupHandlers,
  authStatusHandlers,
  gardenHandlers,
} from "./handlers/authHandlers";
import {
  plantSummariesHandlers,
  wateringMultiplePlantsHandlers,
} from "./handlers/plantHandlers";

export const handlers = [
  ...loginHandlers,
  ...logoutHandlers,
  ...signupHandlers,
  ...authStatusHandlers,
  ...gardenHandlers,
  ...plantSummariesHandlers,
  ...wateringMultiplePlantsHandlers,
];
