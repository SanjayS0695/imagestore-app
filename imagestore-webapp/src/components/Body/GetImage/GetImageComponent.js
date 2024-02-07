import { useEffect, useState } from "react";
import { TextField } from "@mui/material";
import Button from "../../common/Button/Button";
import ImageWrapper from "../../common/ImageWrapper/ImageWrapper";
import "./GetImageComponent.css";
import { useDispatch, useSelector } from "react-redux";
import { GET_IMAGE_BY_ID } from "../../../redux/actions";

const GetImageComponent = () => {
  const dispatch = useDispatch();
  const state = useSelector((state) => state.imageReducer);

  const [searchText, setSearchText] = useState("");
  const [image, setImage] = useState("");

  useEffect(() => {
    if (state?.image) {
      setImage(`data:image/png;base64,${state?.image?.image}`);
    }
  }, [state]);

  const handleSearchText = (event) => {
    setSearchText(event?.target?.value);
  };

  const handleImageSearch = () => {
    dispatch(GET_IMAGE_BY_ID(searchText));
    // var imageData = getImages().filter((data) => data?.id === searchText);
    // if (imageData) {
    //   const totalPath = path + imageData[0]?.filePath;
    //   setImage(totalPath);
    // } else {
    //   setImage("");
    // }
  };

  return (
    <div className="get-image-wrapper">
      <h2>Search Image by Id</h2>
      <div className="get-search-box-wrapper">
        <TextField
          id="outlined-basic"
          variant="outlined"
          label="Type id of the image"
          onChange={handleSearchText}
        ></TextField>
        {searchText && (
          <Button
            label={"Search"}
            handleOnClickAction={handleImageSearch}
          ></Button>
        )}
      </div>
      <ImageWrapper image={image} altText={"Not found."}></ImageWrapper>
    </div>
  );
};

export default GetImageComponent;
