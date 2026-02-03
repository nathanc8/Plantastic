import { useEffect, useState } from "react";
import type EncyclopediaDetailsCardProps from "../types/EncyclopediaCardProps";
import type { PlantDetails } from "../types/PlantDetails";
import { fetchAPI } from "../utils/api";

export default function EncyclopediaDetailsCard({
  selectedPlant,
}: EncyclopediaDetailsCardProps) {
  const [plantDetails, setPlantDetails] = useState<PlantDetails>();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchPlantDetails = async () => {
      if (!selectedPlant) return;
      try {
        const response = await fetchAPI(`/plants/details/${selectedPlant.id}`, {
          method: "GET",
        });
        if (!response.ok) {
          throw new Error("Failed to fetch plant details");
        }
        const data: PlantDetails = await response.json();
        setPlantDetails(data);
      } catch (err) {
        setError(
          err instanceof Error ? err.message : "An unknown error occurred",
        );
      } finally {
        setIsLoading(false);
      }
    };
    fetchPlantDetails();
  }, [selectedPlant]);

  const formatLabel = (key: string) => {
    return key
      .replace(/([A-Z])/g, " $1") // camelCase → mots séparés
      .replace(/^./, (str) => str.toUpperCase()); // majuscule
  };

  return (
    <div>
      {isLoading && <p className="text-white mt-4">Loading plants...</p>}
      {error && <p className="text-text-error mt-4">Error: {error}</p>}
      {!isLoading && !error && plantDetails && (
        <div className="text-black">
          <h2 className="text-2xl font-bold mb-4">{plantDetails.commonName}</h2>
          <img
            src={plantDetails.imageUrl}
            alt={`Picture of ${plantDetails.commonName}`}
            className="w-full h-64 object-cover rounded-lg mb-4"
          />
          <div className="space-y-2">
            {Object.entries(plantDetails)
              .filter(([key, value]) => {
                if (["id", "imageUrl", "commonName"].includes(key)) {
                  return false;
                }

                if (value === null || value === undefined) {
                  return false;
                }

                if (typeof value === "string" && value.trim() === "") {
                  return false;
                }

                return true;
              })
              .map(([key, value]) => (
                <p key={key}>
                  <strong>{formatLabel(key)}:</strong>{" "}
                  {typeof value === "boolean" ? (value ? "Yes" : "No") : value}
                </p>
              ))}
          </div>
        </div>
      )}
    </div>
  );
}
