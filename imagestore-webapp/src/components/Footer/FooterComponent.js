import { useEffect, useState } from "react";
import "./FooterComponent.css";
import { useNavigate } from "react-router-dom";

const FooterComponent = ({ data, handleCancel, handleSubmit }) => {
  const [displayButtons, setDisplayButtons] = useState(false);
  const navigate = useNavigate();
  useEffect(() => {
    if ("" !== data) {
      setDisplayButtons(true);
    } else {
      setDisplayButtons(false);
    }
  }, [data]);

  const handleCancelUpload = () => {
    setDisplayButtons(false);
    handleCancel();
    navigate("/", { replace: true });
  };

  return (
    <div className="footer-wrapper">
      <div className="button-one" onClick={handleSubmit}>
        {displayButtons && <h3>Save</h3>}
      </div>
      <div className="button-two" onClick={handleCancelUpload}>
        {displayButtons && <h3>Cancel</h3>}
      </div>
    </div>
  );
};

export default FooterComponent;
