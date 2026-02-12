import type { ReactNode } from "react";
import logo from "@/assets/img/plantastic1_logo.png";

type DescriptionProps = {
  descriptionTextJSX: ReactNode;
};

export default function Description({ descriptionTextJSX }: DescriptionProps) {
  return (
    <div className=" flex flex-col items-center">
      <img
        src={logo}
        alt="Plantastic logo"
        className="sm:w-36 sm:h-36 lg:w-52 lg:h-52 w-44 h-44 object-contain lg:pb-8 "
      />
      <div className=" leading-[1.5] text-linen block text-sm  lg:text-xl antialiased font-normal text-center font-montserrat">
        {descriptionTextJSX}
      </div>
    </div>
  );
}
