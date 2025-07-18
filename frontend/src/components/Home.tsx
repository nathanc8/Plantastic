import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();
  const handleLogout = () => {
    localStorage.removeItem("authToken");
    navigate("./login", { replace: true });
  };
  return (
    <div>
      <div>
        <h1> C'est gagné! C'est gagné! C'est gagné! Woooo!</h1>
        <h2> Yes, we did it!</h2>
      </div>
      <div>
        <button onClick={handleLogout}>
            Logout
        </button>
      </div>
    </div>
  );
}

export default Home;
