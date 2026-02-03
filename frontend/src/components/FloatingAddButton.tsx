import { useState, type ComponentType, useRef, useEffect } from "react";
import Modal from "./Modal";
import { PiPlantFill } from "react-icons/pi";
import type { IconType } from "react-icons/lib";
import { motion, AnimatePresence } from "framer-motion";
import { Backdrop } from "./Backdrop";

type FloatingAddButtonProps = {
  FormComponent?: ComponentType<{ onClose: () => void }>;
  menuOptions?: Array<{
    FormComponent: ComponentType<{ onClose: () => void }>;
    icon: IconType;
  }>;
};

export default function FloatingAddButton({
  FormComponent,
  menuOptions,
}: FloatingAddButtonProps) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [activeModalIndex, setActiveModalIndex] = useState<number | null>(null);
  const fabRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (fabRef.current && !fabRef.current.contains(event.target as Node)) {
        setIsMenuOpen(false);
      }
    }

    if (isMenuOpen) {
      document.addEventListener("mousedown", handleClickOutside);
      return () =>
        document.removeEventListener("mousedown", handleClickOutside);
    }
  }, [isMenuOpen]);

  const addButtonClass =
    "h-16 w-16 sm:h-20 sm:w-20 xl:h-24 xl:w-24 2xl:h-28 2xl:w-28 absolute -top-6 md:-top-10 xl:-top-12  2xl:-top-14 left-1/2 -translate-x-1/2  bg-forest rounded-full z-30";

  const iconClass =
    "w-8 h-8 md:w-10 md:h-10 lg:w-10 lg:h-10 xl:w-10 xl:h-10 2xl:w-14 2xl:h-14 transition-all group-hover:scale-110";

  return (
    <>
      <Backdrop isOpen={isMenuOpen} onClick={() => setIsMenuOpen(false)} />
      <div className={addButtonClass} ref={fabRef}>
        <button
          onClick={() => {
            if (menuOptions) {
              setIsMenuOpen(!isMenuOpen);
            } else if (FormComponent) {
              setIsModalOpen(true);
            }
          }}
          className="
					rounded-full w-full h-full 
					bg-gradient-to-br from-forest from-50% to-[#232e23]
					hover:from-[#232e23] hover:to-forest
					shadow-lg hover:shadow-xl
					hover:-translate-y-1
					transition duration-700
					flex items-center justify-center
					group
				"
        >
          <PiPlantFill
            className={`${iconClass} transition-colors ${
              isMenuOpen ? "text-active" : ""
            }`}
          />
        </button>

        <AnimatePresence>
          {isMenuOpen && menuOptions && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="absolute bottom-full mb-4 left-1/2 -translate-x-1/2 flex flex-col gap-3"
            >
              {menuOptions.map((option, index) => (
                <motion.button
                  key={index}
                  initial={{ opacity: 0, scale: 0, y: 20 }}
                  animate={{ opacity: 1, scale: 1, y: 0 }}
                  transition={{
                    delay: index * 0.1,
                    type: "spring",
                    stiffness: 260,
                    damping: 20,
                  }}
                  onClick={() => {
                    setActiveModalIndex(index);
                    setIsMenuOpen(false);
                  }}
                  className="
            w-12 h-12 sm:w-14 sm:h-14 xl:w-16 xl:h-16
            rounded-full 
            bg-gradient-to-br from-forest to-[#1a221a]
            hover:from-[#1a221a] hover:to-forest
            shadow-lg hover:shadow-xl
            transition-colors duration-300
            flex items-center justify-center
          "
                  whileHover={{ scale: 1.1 }}
                  whileTap={{ scale: 0.95 }}
                >
                  <option.icon className="w-6 h-6 sm:w-7 sm:h-7 xl:w-8 xl:h-8 text-white" />
                </motion.button>
              ))}
            </motion.div>
          )}
        </AnimatePresence>

        {menuOptions && activeModalIndex !== null && (
          <Modal
            isOpen={true}
            onClose={() => setActiveModalIndex(null)}
            size="lg"
          >
            {(() => {
              const SelectedForm = menuOptions[activeModalIndex].FormComponent;
              return <SelectedForm onClose={() => setActiveModalIndex(null)} />;
            })()}
          </Modal>
        )}

        <Modal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          size="lg"
        >
          {FormComponent && (
            <FormComponent onClose={() => setIsModalOpen(false)} />
          )}{" "}
        </Modal>
      </div>
    </>
  );
}
