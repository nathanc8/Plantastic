import { useEffect, useState } from "react";
import type { UserPlantDetails } from "../types/UserPlantDetails";
import { fetchAPI } from "../utils/api";
import WaterOnePlantModal from "./WaterOnePlantModal";
import Modal from "./Modal";
import DeletePlantModal from "./DeletePlantModal";

export default function PlantDetailsModal({ plantId }: { plantId: number }) {
  const [plantDetails, setPlantDetails] = useState<UserPlantDetails | null>(
    null,
  );
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isWaterModalOpen, setIsWaterModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const baseButtonClass = "mt-1 px-3 py-3 text-white rounded text-xl";
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetchAPI(`/user-plants/details/${plantId}`, {
          method: "GET",
        });
        if (response.ok) {
          const data = await response.json();
          setPlantDetails(data);
        } else {
          setError(`Failed to load plant details (${response.status})`);
        }
      } catch (error) {
        setError(
          error instanceof Error ? error.message : "An unknown error occurred",
        );
      } finally {
        setIsLoading(false);
      }
    };
    fetchData();
  }, [plantId]);

  return (
    <div>
      {isLoading && <p className="text-text-placeholder"> ⏳ Loading...</p>}
      {error && <p className="text-text-error mt-4">Error: {error}</p>}
      {!isLoading && !error && plantDetails && (
        <>
          <div>
            <h2 className="text-2xl font-bold mb-4">{plantDetails.nickname}</h2>
            <div className="flex ">
              <img
                className="w-50 h-64 object-cover rounded-lg mb-4"
                src={
                  plantDetails.userPlantImageUrl || plantDetails.plantImageUrl
                }
                alt="Plant picture"
              />
              <div className="ml-6 text-xl content-center">
                <h3>
                  <strong>Common name:</strong> {plantDetails.commonName}
                </h3>
                <p>
                  <strong>Scientific name:</strong>{" "}
                  {plantDetails.scientificName}{" "}
                </p>
                <p>
                  <strong>Last Watering:</strong>{" "}
                  {plantDetails.lastWatering}{" "}
                </p>
                <p>
                  <strong>Next watering:</strong>{" "}
                  {plantDetails.nextWatering}{" "}
                </p>
                <p>
                  <strong>Watering frequency:</strong> every{" "}
                  {plantDetails.waterFreq} days
                </p>
                <p>
                  <strong>Light exposure:</strong>{" "}
                  {plantDetails.lightExposure}{" "}
                </p>
              </div>
            </div>
          </div>
          <div className="grid grid-cols-2 content-center gap-4">
            <button
              onClick={() => setIsWaterModalOpen(true)}
              className={`${baseButtonClass} bg-sage hover:bg-sage-dark`}
            >
              Water the plant
            </button>
            <button
              onClick={() => setIsDeleteModalOpen(true)}
              className={`${baseButtonClass} bg-clay hover:bg-clay-dark`}
            >
              Delete this plant
            </button>
          </div>

          <Modal
            isOpen={isWaterModalOpen}
            onClose={() => setIsWaterModalOpen(false)}
            size="lg"
          >
            <WaterOnePlantModal
              onClose={() => setIsWaterModalOpen(false)}
              plantId={plantId}
              plantNickname={plantDetails.nickname}
              plantCommonName={plantDetails.commonName}
            ></WaterOnePlantModal>
          </Modal>

          <Modal
            isOpen={isDeleteModalOpen}
            onClose={() => setIsDeleteModalOpen(false)}
            size="lg"
          >
            <DeletePlantModal
              onClose={() => setIsDeleteModalOpen(false)}
              plantId={plantId}
            ></DeletePlantModal>
          </Modal>
        </>
      )}
    </div>
  );
}
