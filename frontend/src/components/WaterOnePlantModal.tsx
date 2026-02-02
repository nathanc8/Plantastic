import { useState } from "react";
import { useGarden } from "../context/GardenContext";
import { getTodayLocal } from "../utils/date";
import { fetchAPI } from "../utils/api";
import toast from "react-hot-toast";
import { type WateringOneModalProps } from "../types/WateringOneModalProps";

const WateringOneModal = ({
  onClose,
  plantId,
  plantNickname,
  plantCommonName,
}: WateringOneModalProps) => {
  const { refreshGarden } = useGarden();
  const [wateringDate, setWateringDate] = useState(new Date());
  const today = getTodayLocal();

  const baseButtonClass = "mt-1 px-3 py-3 text-white rounded text-xl";

  const handleWatering = async () => {
    try {
      const response = await fetchAPI(
        `/user-plants/water-one/${plantId}?date=${
          wateringDate.toISOString().split("T")[0]
        }`,
        {
          method: "PATCH",
          body: JSON.stringify(plantId),
        },
      );

      const data = await response.json();

      if (!response.ok) {
        toast(`Error: ${data || "Error saving data"}`);
      } else {
        await refreshGarden();
        onClose();
        toast(`Your ${plantNickname || plantCommonName} is no longer thirsty`, {
          icon: "🌿",
        });
      }
    } catch (error) {
      toast.error(`Error: ${error}`);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col items-center gap-4">
        <h2 className="text-xl font-bold text-center">
          When did you water your {plantNickname || plantCommonName}?
        </h2>
        <input
          type="date"
          value={wateringDate.toISOString().split("T")[0]}
          max={today}
          onChange={(e) => setWateringDate(new Date(e.target.value))}
          className="px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-[#4f674f] text-lg"
        />
      </div>
      <div className="grid grid-cols-2 content-center gap-4">
        <button
          onClick={handleWatering}
          className={`${baseButtonClass} bg-[#4f674f] hover:bg-[#232c23]`}
        >
          Confirm
        </button>
        <button
          onClick={onClose}
          className={`${baseButtonClass} bg-[#8B4509] hover:bg-[#7A3D08]`}
        >
          Cancel
        </button>
      </div>
    </div>
  );
};

export default WateringOneModal;
