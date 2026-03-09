import { useState } from "react";
import type { UserPlant } from "../types/UserPlant";
import Modal from "./Modal";
import PlantDetailsModal from "./PlantDetailsModal";

export default function PlantCard({ plant }: { plant: UserPlant }) {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const [year, month, day] = plant.nextWatering.split("-").map(Number);
  const nextWateringDate = new Date(year, month - 1, day);
  const isThirsty = nextWateringDate < today;
  return (
    <article
      id="plant-card"
      className="w-full max-w-sm h-36 flex bg-linen rounded-lg p-2 shadow-lg transform transition-all hover:-translate-y-1 duration-300 hover:shadow"
    >
      <img
        src={plant.userPlantImageUrl || plant.plantImageUrl}
        alt={`Picture of ${plant.nickname}`}
        className="w-28 h-28 rounded-lg object-cover"
      />

      <div
        id="plant-card-desc"
        className="flex-1 flex flex-col font-bellota p-2 justify-between"
      >
        {/* Infos compactes */}
        <div>
          <h3 className="text-sm font-bold text-text-primary truncate">
            {plant.nickname}
          </h3>
          <p className="text-xs text-text-secondary italic truncate">
            Common name: {plant.commonName}
          </p>
          <p className="text-xs text-text-secondary truncate">
            Last watering: {plant.lastWatering}
          </p>
          <p className="text-xs text-text-secondary truncate">
            Next watering: {plant.nextWatering}
          </p>
          {isThirsty && (
            <p className="text-text-error font-bold" role="alert">
              I feel thirsty !
            </p>
          )}
        </div>
        <button
          onClick={() => setIsModalOpen(true)}
          className="mt-1 px-3 py-1 text-xs bg-sage text-white rounded hover:bg-sage-dark w-full"
          aria-label={`Consult ${plant.nickname} details`}
        >
          Consult
        </button>
        <Modal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          size="lg"
        >
          <PlantDetailsModal plantId={plant.id}></PlantDetailsModal>
        </Modal>
      </div>
    </article>
  );
}
