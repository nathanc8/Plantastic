// import React from 'react'
import BackgroundWrapper from "../components/BackgroundWrapper";
import BottomNavBar from "../components/BottomNavBar";
import { Header } from "../components/Header";

const Advices = () => {
  return (
    <BackgroundWrapper>
      <Header />
      <h1 className="text-white flex justify-center font-montserrat">
        This is the Advices page 🧙🏽‍♀️
      </h1>
      <p className="text-white flex justify-center font-montserrat mt-5">
        🚧 Patience, we are working on it 🚧
      </p>
      <BottomNavBar />
    </BackgroundWrapper>
  );
};

export default Advices;
