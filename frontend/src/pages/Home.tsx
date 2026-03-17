import BackgroundWrapper from "../components/BackgroundWrapper";
import BottomNavBar from "../components/BottomNavBar";
import { Header } from "../components/Header";
import PlantCard from "../components/PlantCard";
// import { useAuth } from "../context/AuthContext";
import { useGarden } from "../context/GardenContext";

export default function Home() {
  const { plants, isLoading } = useGarden();

  if (isLoading) {
    return (
      <BackgroundWrapper>
        <div className="flex items-center justify-center h-screen">
          <p className="text-text-secondary"> ⏳ Loading...</p>
        </div>
      </BackgroundWrapper>
    );
  }

  return (
    <BackgroundWrapper>
      <BottomNavBar />
      <Header />
      <main className="flex flex-col items-center w-full px-4 pt-4  pb-60">
        <div id="all-cards" className="flex flex-wrap gap-3 justify-center">
          {plants.length > 0 ? (
            plants.map((plant) => <PlantCard key={plant.id} plant={plant} />)
          ) : (
            <p className="text-white">No plants yet. Add your first plant!</p>
          )}
        </div>
      </main>
    </BackgroundWrapper>
  );
}
