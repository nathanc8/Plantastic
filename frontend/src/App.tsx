import { Routes, Route } from "react-router-dom";
import Login from "./components/Login";
//import SignUp from "./components/Signup";
import Home from "./components/Home";
import ProtectedRoutes from "./utils/ProtectedRoutes";

export function App() {
  return (
    <>
      <Routes>
        {/* <Route path="/signup" element={<SignUp />} /> */}
        <Route path="/login" element={<Login />} />
        <Route element={<ProtectedRoutes/>}>
          <Route path="/" element={<Home />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;

//   return (
//     <>
//       <h1>Routing Test</h1>
//       <Routes>
//         <Route path="/login" element={<Login />} />
//         <Route path="/" element={<Home />} />
//       </Routes>
//     </>
//   );