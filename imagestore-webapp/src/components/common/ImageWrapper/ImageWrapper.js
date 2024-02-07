import "./ImageWrapper.css";

const ImageWrapper = ({ altText, image }) => {
  return (
    <div className="wrapper">{image && <img alt={altText} src={image} />}</div>
  );
};

export default ImageWrapper;
