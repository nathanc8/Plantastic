import toast from "react-hot-toast";
import { fetchAPI } from "../utils/api";
import { useGarden } from "../context/GardenContext";
import { type DeletePlantModalProps } from "../types/DeletePlantModalProps";

const DeletePlantModal = ({ onClose, plantId }: DeletePlantModalProps) => {
  const { refreshGarden } = useGarden();

  const baseButtonClass = "mt-1 px-3 py-3 text-white rounded text-xl";

  const handleDelete = async () => {
    try {
      const response = await fetchAPI(`/user-plants/delete-one/${plantId}`, {
        method: "DELETE",
      });
      const data = await response.json();

      if (!response.ok) {
        toast.error(`Error: ${data || "Error deleting data"}`);
      } else {
        await refreshGarden();
        onClose();
        toast(`Your plant has been deleted`, {
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
          Do you want to delete this plant?
        </h2>
      </div>
      <div className="grid grid-cols-2 content-center gap-4">
        <button
          onClick={onClose}
          className={`${baseButtonClass} bg-sage hover:bg-sage-dark`}
        >
          Cancel
        </button>
        <button
          onClick={handleDelete}
          className={`${baseButtonClass} bg-clay hover:bg-clay-dark`}
        >
          Confirm
        </button>
      </div>
    </div>
  );
};

export default DeletePlantModal;
