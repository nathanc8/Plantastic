import Modal from "./Modal";

interface LegalModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function LegalModal({ isOpen, onClose }: LegalModalProps) {
  return (
    <Modal isOpen={isOpen} onClose={onClose} size="lg">
      <div className="text-white space-y-6 p-2">
        <h2 className="text-xl font-bold text-sage">Mentions légales</h2>

        {/* Cookies */}
        <section>
          <h3 className="text-lg font-bold mb-2 text-sage">Cookies usage</h3>
          <p className="text-text-secondary text-sm leading-relaxed">
            Plantastic uses session cookies to maintain your login state across
            pages. These cookies are strictly necessary for the application to
            function and are not used for advertising or tracking purposes.
          </p>
          <p className="text-text-secondary text-sm leading-relaxed mt-2">
            No personal data is shared with third parties. Cookies are
            automatically deleted when you log out or when the session expires.
          </p>
        </section>

        {/* Account deletion */}
        <section>
          <h3 className="text-lg font-semibold mb-2 text-sage">
            Account Deletion
          </h3>
          <p className="text-text-secondary text-sm leading-relaxed">
            In accordance with the RGPD, you have the right to the deletion of
            your personal data. To request the deletion of your account and all
            your data, please contact us by email at the following address:
          </p>
          <a
            href="mailto:cazard.nathan@gmail.com"
            className="inline-block mt-2 text-sage underline text-sm hover:opacity-80 transition-opacity"
          >
            cazard.nathan@gmail.com
          </a>
          <p className="text-text-secondary text-sm leading-relaxed mt-2">
            Your request will be processed within 30 days.
          </p>
        </section>

        {/* Editor */}
        <section>
          <h3 className="text-lg font-semibold mb-2 text-sage">Editor</h3>
          <p className="text-text-secondary text-sm leading-relaxed">
            Plantastic is a personal application developed within the context of
            a professional certification project. For any questions, please
            contact us at the address indicated above.
          </p>
        </section>

        <button
          onClick={onClose}
          className="mt-4 w-full py-2 rounded-lg bg-sage font-semibold hover:opacity-90 transition-opacity"
        >
          Close
        </button>
      </div>
    </Modal>
  );
}
