import { useEffect, useState } from "react";
import ImageWrapper from "../../common/ImageWrapper/ImageWrapper";
import { VIEW_ALL_IMAGES } from "../../../redux/actions";
import { useDispatch, useSelector } from "react-redux";
import "./ViewComponent.css";

const ViewComponent = () => {
  const [imageList, setImageList] = useState([]);
  const dispatch = useDispatch();
  const state = useSelector((state) => state.imageReducer);

  useEffect(() => {
    dispatch(VIEW_ALL_IMAGES());
  }, []);

  useEffect(() => {
    console.log(state?.images);
    if (state?.images?.length > 0) {
      setImageList(state?.images);
    }
  }, [state]);

  return (
    <div className="view-wrapper">
      {imageList.length > 0 && (
        <>
          <h2>Scroll to view all uploaded images</h2>
          <div className="card-container">
            <div className="scroll-box">
              {imageList?.map((item) => (
                <div className="card-wrapper" key={item?.id}>
                  <div className="image-wrapper">
                    <ImageWrapper
                      altText={"Not found"}
                      image={`data:image/png;base64,${item?.image}`}
                    ></ImageWrapper>
                  </div>
                  <div className="card-title">Name: {item?.name}</div>
                  <div className="card-id">ID: {item?.id}</div>
                </div>
              ))}
            </div>
          </div>
        </>
      )}
      {imageList.length === 0 && (
        <>
          <h1>EMPTY STORE</h1>
          <h3>Upload photos to view them here.</h3>
        </>
      )}
    </div>
  );
};

export default ViewComponent;
