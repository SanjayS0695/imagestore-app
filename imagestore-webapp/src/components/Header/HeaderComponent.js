import "./HeaderComponent.css";

const HeaderComponent = ({ isAuthPage, handleLogout }) => {
  return (
    <div className="header">
      <div className="title-wrapper">
        <h2>Image Store</h2>
      </div>
      {!isAuthPage && (
        <div className="logout-wrapper">
          <input
            className="logout-button"
            type="button"
            onClick={handleLogout}
            value={"Log out"}
          />
        </div>
      )}
    </div>
  );
};

export default HeaderComponent;
