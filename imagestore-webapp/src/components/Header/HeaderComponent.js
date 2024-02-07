import logo from "../../images/logo.png";
import "./HeaderComponent.css";

const HeaderComponent = (isAuthPage, handleLogout) => {
  return (
    <div className="header">
      <div className="logo-wrapper">
        <img src={logo} className="App-logo" alt="logo" />
      </div>
      <div className="title-wrapper">
        <h2>Image Store</h2>
      </div>
      {!isAuthPage && (
        <div className="logout-wrapper">
          <button onClick={handleLogout}>Log Out</button>
        </div>
      )}
    </div>
  );
};

export default HeaderComponent;
