import type { PlantSummary } from "../types/PlantSummary";

type PlantCardEncyclopediaProps = {
  plant: PlantSummary;
  onClick: () => void;
};

export default function PlantCardEncyclopedia({
  plant,
  onClick,
}: PlantCardEncyclopediaProps) {
  const descriptionSnippet =
    plant.description && plant.description.length > 200
      ? `${plant.description.substring(0, 200)}...`
      : plant.description;

  return (
    <div
      role="button"
      tabIndex={0}
      onClick={onClick}
      onKeyDown={(e) => {
        if (e.key === "Enter" || e.key === " ") {
          e.preventDefault();
          onClick();
        }
      }}
      className="w-full h-full flex flex-col bg-linen rounded-lg p-4 shadow-lg transform transition-all hover:-translate-y-1 duration-300 hover:shadow-xl cursor-pointer"
    >
      <div
        className="w-full h-48 bg-cover bg-center rounded-t-lg"
        style={{ backgroundImage: `url(${plant.imageUrl})` }}
      />
      <div className="flex-1 flex flex-col font-bellota p-2 justify-between">
        <div>
          <h3 className="text-lg font-bold text-text-primary truncate">
            {plant.commonName}
          </h3>
          <p className="text-sm text-text-secondary italic truncate">
            {plant.scientificName}
          </p>
          <p className="text-sm text-text-secondary mt-2">
            {descriptionSnippet}
          </p>
        </div>
        <p className="text-sm text-text-secondary mt-2">
          Watering: {plant.watering}
        </p>
      </div>
    </div>
  );
}
