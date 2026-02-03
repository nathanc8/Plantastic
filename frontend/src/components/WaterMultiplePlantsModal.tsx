import { useState } from "react";
import { useGarden } from "../context/GardenContext";
import { fetchAPI } from "../utils/api";
import toast from "react-hot-toast";
import { getTodayLocal } from "../utils/date";

export const WaterMultiplePlantsModal = ({
  onClose,
}: {
  onClose: () => void;
}) => {
  const { plants, refreshGarden } = useGarden();
  const [selectedPlantIds, setSelectedPlantIds] = useState<number[]>([]);
  const [wateringDate, setWateringDate] = useState(new Date());
  const today = getTodayLocal();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleCheckboxChange = (plantId: number, isChecked: boolean) => {
    if (isChecked) {
      setSelectedPlantIds([...selectedPlantIds, plantId]);
    } else {
      setSelectedPlantIds(selectedPlantIds.filter((id) => id !== plantId));
    }
  };

  const handleWatering = async () => {
    if (isSubmitting) return;
    setIsSubmitting(true);

    try {
      const response = await fetchAPI(
        `/user-plants/water-multiples?date=${
          wateringDate.toISOString().split("T")[0]
        }`,
        {
          method: "PATCH",
          body: JSON.stringify(selectedPlantIds),
        },
      );

      const data = await response.json();

      if (!response.ok) {
        toast(`Error: ${data || "Error saving data"}`);
      } else {
        await refreshGarden();
        setTimeout(() => onClose(), 2000);
        toast("Your plants are no longer thirsty", { icon: "🌿" });
      }
    } catch (error) {
      toast(`Error: ${error}`);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSelectAll = () => {
    if (selectedPlantIds.length === plants.length) {
      setSelectedPlantIds([]);
    } else {
      setSelectedPlantIds(plants.map((plant) => plant.id));
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold text-gray-900 mb-4 font">
        Water multiple plants
      </h2>
      <div
        role="status"
        aria-live="polite"
        className="text-sm text-gray-600 mb-3"
      >
        {selectedPlantIds.length === 0
          ? "No plants selected"
          : selectedPlantIds.length === 1
            ? "1 plant selected"
            : `${selectedPlantIds.length} plants selected`}
      </div>
      {plants.length > 0 ? (
        <fieldset className="mb-4">
          <legend className="sr-only">Select plants to water</legend>
          <div className="space-y-2 max-h-60 overflow-y-auto border rounded p-2">
            {plants.map((plant) => (
              <div key={plant.id} className="flex items-center">
                <input
                  type="checkbox"
                  id={`plant-${plant.id}`}
                  checked={selectedPlantIds.includes(plant.id)}
                  onChange={(e) =>
                    handleCheckboxChange(plant.id, e.target.checked)
                  }
                  className="w-4 h-4 mr-3 focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                />
                <label
                  htmlFor={`plant-${plant.id}`}
                  className="flex-1 cursor-pointer"
                >
                  <span className="font-medium">{plant.nickname}</span>
                  {", "}
                  <span className="text-gray-600">{plant.commonName}</span>
                  {" - "}
                  <span className="text-sm text-gray-500">
                    Last watering: {plant.lastWatering}
                  </span>
                </label>
              </div>
            ))}
          </div>
        </fieldset>
      ) : (
        <p>You have no plant to water!</p>
      )}
      <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center mt-6">
        <div className="flex-1">
          <label
            htmlFor="watering-date"
            className="block text-sm font-medium text-gray-900 mb-1"
          >
            Watering date
          </label>
          <input
            id="watering-date"
            type="date"
            value={wateringDate.toISOString().split("T")[0]}
            max={today}
            onChange={(e) => setWateringDate(new Date(e.target.value))}
            className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button
          onClick={handleSelectAll}
          className="px-4 py-2 bg-[#4F674F] text-white font-montserrat rounded hover:bg-green-950 focus:ring-2 focus:ring-blue-500 focus:outline-none"
          aria-label={
            selectedPlantIds.length === plants.length
              ? "Deselect all"
              : "Select all"
          }
        >
          {selectedPlantIds.length === plants.length
            ? "Deselect all"
            : "Select all"}
        </button>
      </div>

      <button
        onClick={(e) => {
          if (selectedPlantIds.length === 0) {
            e.preventDefault();
            return;
          }
          handleWatering();
        }}
        aria-disabled={selectedPlantIds.length === 0 || isSubmitting}
        className={`w-full font-montserrat font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-6 mb-1 text-white bg-[#4F674F] hover:bg-green-950 focus:ring-2 focus:ring-blue-500 focus:outline-none ${
          selectedPlantIds.length === 0 || isSubmitting
            ? "opacity-50 cursor-not-allowed"
            : ""
        }`}
      >
        {isSubmitting
          ? "Watering..."
          : selectedPlantIds.length === 0
            ? "Water your plants"
            : selectedPlantIds.length === 1
              ? "Water 1 plant"
              : `Water ${selectedPlantIds.length} plants`}
      </button>
    </div>
  );
};
