import { useState } from "react";
import type { FormProps } from "../types/FormProps";
import { fetchAPI } from "../utils/api";
import { useForm, type SubmitHandler } from "react-hook-form";
import { addPlantToEncyclopediaSchema } from "../schemas/addPlantToEncyclopediaSchema";
import { zodResolver } from "@hookform/resolvers/zod";
import { type ReactNode } from "react";
import type { AddPlantToEncyclopediaDto } from "../dto/AddPlantToEncyclopediaDto";

export default function AddEncyclopediaPlantForm({
  onClose,
  onRefresh,
}: FormProps & { onRefresh?: () => void }) {
  const [plantPictureFile, setPlantPictureFile] = useState<File | null>(null);
  const [plantPicture, setPlantPicture] = useState<string | null>(null);
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [_apiMessage, setApiMessage] = useState<ReactNode>(null);
  // TODO: Keyboard navigation
  // const [activeIndex, setActiveIndex] = useState(-1);

  const { register, handleSubmit, formState } =
    useForm<AddPlantToEncyclopediaDto>({
      resolver: zodResolver(addPlantToEncyclopediaSchema),
    });

  // CALL FOR PLANT CREATION
  const onSubmit: SubmitHandler<AddPlantToEncyclopediaDto> = async (
    formData: AddPlantToEncyclopediaDto,
  ) => {
    if (!plantPicture) return;
    try {
      const dataToSend = {
        commonName: formData.commonName,
        otherName: formData.otherName,
        scientificName: formData.scientificName,
        family: formData.family,
        description: formData.description,
        careLevel: formData.careLevel,
        watering: formData.watering,
        soil: formData.soil,
        lightExposure: formData.lightExposure,
        growthRate: formData.growthRate,
        poisonousToPet: formData.poisonousToPet,
        wateringDetails: formData.wateringDetails,
        sunlightDetails: formData.sunlightDetails,
        pruningDetails: formData.pruningDetails,
      };

      const formDataToSend = new FormData();
      formDataToSend.append(
        "data",
        new Blob([JSON.stringify(dataToSend)], { type: "application/json" }),
      );
      if (plantPictureFile) {
        formDataToSend.append("file", plantPictureFile);
      }

      const response = await fetchAPI("/plants/add-one", {
        method: "POST",
        body: formDataToSend,
      });
      const data = await response.text();
      if (!response.ok) {
        setApiMessage(`Error: ${data || "Error saving data"}`);
      } else {
        if (onRefresh) onRefresh();
        onClose();
      }
    } catch (error) {
      console.error(error);
      setApiMessage("An error occured.");
    }
  };

  // Display user-uploaded picture or fallback to plant's default image
  const imageToDisplay = plantPicture;

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 ">
      <h1 className="text-2-xl text-black font-bold mb-4">
        Add a plant to encyclopedia
      </h1>
      {/* COMMON NAME */}
      <label className="block text-sm font-medium">Common name *</label>
      <input
        {...register("commonName", { required: true })}
        type="text"
        className="w-full px-3 py-2 border rounded-lg"
      />

      {/* OTHER NAME */}
      <label className="block text-sm font-medium">Other name</label>
      <input
        {...register("otherName")}
        type="text"
        className="w-full px-3 py-2 border rounded-lg"
      />

      {/* SCIENTIFIC NAME */}
      <label className="block text-sm font-medium">Scientific name *</label>
      <input
        {...register("scientificName", { required: true })}
        type="text"
        className="w-full px-3 py-2 border rounded-lg"
      />

      {/* FAMILY */}
      <label className="block text-sm font-medium">Family</label>
      <input
        {...register("family")}
        type="text"
        className="w-full px-3 py-2 border rounded-lg"
      />

      {/* DESCRIPTION */}
      <label className="block text-sm font-medium">Description</label>
      <textarea
        {...register("description")}
        className="w-full px-3 py-2 border rounded-lg"
      />

      {/* CARE LEVEL */}
      <label className="block text-sm font-medium">Care level</label>
      <select
        {...register("careLevel")}
        className="w-full px-3 py-2 border rounded-lg"
      >
        <option value="">Select</option>
        <option value="Easy">Easy</option>
        <option value="Medium">Medium</option>
        <option value="Hard">Hard</option>
      </select>

      {/* WATERING */}
      <label className="block text-sm font-medium">Watering</label>
      <select
        {...register("watering")}
        className="w-full px-3 py-2 border rounded-lg"
      >
        <option value="">Select</option>
        <option value="Frequent">Frequent (5 days)</option>
        <option value="Average">Average (10 days)</option>
        <option value="Minimum">Minimum (20 days)</option>
      </select>

      {/* SOIL */}
      <label className="block text-sm font-medium">Soil</label>
      <input
        {...register("soil")}
        type="text"
        className="w-full px-3 py-2 border rounded-lg"
      />

      {/* LIGHT EXPOSURE */}
      <label className="block text-sm font-medium">Light exposure</label>
      <input
        {...register("lightExposure")}
        type="text"
        className="w-full px-3 py-2 border rounded-lg"
      />

      {/* GROWTH RATE */}
      <label className="block text-sm font-medium">Growth rate</label>
      <input
        {...register("growthRate")}
        type="text"
        className="w-full px-3 py-2 border rounded-lg"
      />

      {/* POISONOUS */}
      <label className="flex items-center gap-2 mt-2">
        <input type="checkbox" {...register("poisonousToPet")} />
        Poisonous to pets
      </label>

      {/* PICTURE  */}
      <label className="block text-sm text-black font-medium  mt-5 mb-1">
        Picture:
      </label>
      <div className="h-36 w-36">
        {imageToDisplay && <img src={imageToDisplay} alt="Plant" />}
      </div>
      <input
        type="file"
        accept="image/*"
        onChange={(e) => {
          const file = e.target.files?.[0];
          if (file) {
            setPlantPictureFile(file);
            setPlantPicture(URL.createObjectURL(file));
          }
        }}
        className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 mb-5"
      />

      {/* DETAILS */}
      <label className="block text-sm font-medium">Watering details</label>
      <textarea
        {...register("wateringDetails")}
        className="w-full border rounded-lg"
      />

      <label className="block text-sm font-medium">Sunlight details</label>
      <textarea
        {...register("sunlightDetails")}
        className="w-full border rounded-lg"
      />

      <label className="block text-sm font-medium">Pruning details</label>
      <textarea
        {...register("pruningDetails")}
        className="w-full border rounded-lg"
      />

      {/* ERRORS */}
      {Object.keys(formState.errors).length > 0 && (
        <div className="bg-red-100 p-2 rounded">
          {JSON.stringify(formState.errors)}
        </div>
      )}

      <button
        type="submit"
        className="w-full bg-sage hover:bg-sage-dark text-white py-2 rounded-lg"
      >
        Send
      </button>
    </form>
  );
}
