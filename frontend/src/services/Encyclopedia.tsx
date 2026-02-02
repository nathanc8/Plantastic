// import React from 'react'
import { useCallback, useEffect, useMemo, useState } from "react";
import BackgroundWrapper from "../components/BackgroundWrapper";
import BottomNavBar from "../components/BottomNavBar";
import { Header } from "../components/Header";
import { fetchAPI } from "../utils/api";
import type { PlantSummary } from "../types/PlantSummary.ts";
import PlantCardEncyclopedia from "../components/EncyclopediaSummaryCard";
import Modal from "../components/Modal";
import EncyclopediaDetailsCard from "../components/EncyclopediaDetailsCard.tsx";

export default function Encyclopedia() {
  const [plants, setPlants] = useState<PlantSummary[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedPlant, setSelectedPlant] = useState<PlantSummary | null>(null);
  const [searchQuery, setSearchQuery] = useState("");

  const fetchPlants = useCallback(async () => {
    try {
      const response = await fetchAPI("/plants/encyclopedia", {
        method: "GET",
      });
      if (!response.ok) {
        throw new Error("Failed to fetch plants");
      }
      const data: PlantSummary[] = await response.json();
      setPlants(data);
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "An unknown error occurred",
      );
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchPlants();
  }, [fetchPlants]);

  const handleCardClick = (plant: PlantSummary) => {
    setSelectedPlant(plant);
  };

  const closeModal = () => {
    setSelectedPlant(null);
  };

  const filteredPlants = useMemo(() => {
    const query = searchQuery.toLowerCase();

    return plants.filter((plant) => {
      const commonName = plant.commonName?.toLowerCase() || "";
      const scientificName = plant.scientificName.toLowerCase();
      return commonName.includes(query) || scientificName.includes(query);
    });
  }, [plants, searchQuery]);

  return (
    <BackgroundWrapper>
      <Header />
      <main className="flex flex-col items-center w-full px-4">
        <div className="sticky top-0 z-10 w-full flex justify-center bg-transparent pb-14 pt-4 ">
          <input
            type="text"
            placeholder="Search a plant by common name..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full max-w-lg p-3 text-white bg-[#2D3D2D] border border-gray-600 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-yellow-200 opacity-95"
          />
        </div>
        <a
          href="#navbar"
          className="sr-only focus:not-sr-only focus:absolute focus:top-4° focus:left-4 focus:z-50 focus:bg-yellow-200 focus:text-black focus:px-4 focus:py-2 focus:rounded"
        >
          Skip to navigation
        </a>
        {isLoading && <p className="text-white mt-4">Loading plants...</p>}
        {error && <p className="text-red-400 mt-4">Error: {error}</p>}
        {!isLoading && !error && filteredPlants.length === 0 && (
          <p className="text-gray-400 mt-6">No plants found 🌱</p>
        )}
        {!isLoading && !error && (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 w-full ">
            {filteredPlants.map((plant) => (
              <PlantCardEncyclopedia
                key={plant.id}
                plant={plant}
                onClick={() => handleCardClick(plant)}
              />
            ))}
          </div>
        )}
      </main>
      {selectedPlant && (
        <Modal isOpen={!!selectedPlant} onClose={closeModal} size="lg">
          <EncyclopediaDetailsCard selectedPlant={selectedPlant} />
        </Modal>
      )}
      <BottomNavBar onRefresh={fetchPlants} />
    </BackgroundWrapper>
  );
}
