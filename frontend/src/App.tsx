import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import SignUp from "./pages/SignUp";
import ProtectedRoutes from "./utils/ProtectedRoutes";
import Encyclopedia from "./pages/Encyclopedia";
import Advices from "./pages/Advices";
import { Toaster } from "react-hot-toast";

export function App() {
  return (
    <>
      <Toaster />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        {/* PROTECTED PAGES  */}
        <Route element={<ProtectedRoutes />}>
          <Route path="/" element={<Home />} />
          <Route path="/encyclopedia" element={<Encyclopedia />} />
          <Route path="/advices" element={<Advices />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
