export const addAuthHeader = () => {
  const user = JSON.parse(localStorage.getItem("user"));

  if (user && user?.accessToken) {
    return { Authorization: "Bearer " + user?.accessToken };
  } else {
    return {};
  }
};

export const getRefreshToken = () => {
  const user = JSON.parse(localStorage.getItem("user"));

  if (user && user?.refreshToken) {
    return user?.refreshToken;
  } else {
    return "";
  }
};
