// import React from 'react'
import BackgroundWrapper from "../components/BackgroundWrapper";
import BottomNavBar from "../components/BottomNavBar";
import { Header } from "../components/Header";
import LegalFooter from "../components/LegalFooter";

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
      <LegalFooter />
    </BackgroundWrapper>
  );
};

export default Advices;
