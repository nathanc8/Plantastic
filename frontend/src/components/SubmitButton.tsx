import clsx from "clsx";

type SubmitButtonProps = {
  loading?: boolean;
  text: string;
};

export default function SubmitButton({ loading, text }: SubmitButtonProps) {
  return (
    <button
      type="submit"
      disabled={loading}
      className={clsx(
        "w-full font-montserrat font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-3 mb-1 transition-all duration-200",
        "text-white bg-sage hover:bg-sage-dark",
        loading && "opacity-60 cursor-not-allowed",
      )}
    >
      {loading ? "Loading..." : text}
    </button>
  );
}
