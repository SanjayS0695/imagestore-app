import { useState } from "react";
import "./UploadComponent.css";

const UploadComponent = ({ handleImage, image }) => {
  const [displayImage, setDisplayImage] = useState(null);

  const handleUpload = (event) => {
    handleImage(event?.target?.files[0]);
    setDisplayImage(URL.createObjectURL(event?.target?.files[0]));
  };

  return (
    <div className="upload-wrapper">
      <h2>Select image to upload</h2>
      <input type="file" onChange={handleUpload} />
      <div className="image-wrapper">
        <img alt="Not found" src={displayImage} />
      </div>
    </div>
  );
};

export default UploadComponent;
