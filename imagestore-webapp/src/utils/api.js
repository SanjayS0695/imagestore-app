import axios from "axios";
import { addAuthHeader, getRefreshToken } from "./helper";

const API_URL = "http://localhost:8080/api/auth";

const client = axios.create({
  Accept: "application/json",
  "Content-Type": "application/json",
});

client.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const refreshToken = getRefreshToken();
        if (refreshToken) {
          const response = await axios.post(`${API_URL}/refresh-token`, {
            refreshToken,
          });
          const token = response?.data?.accessToken;

          localStorage.setItem("user", JSON.stringify(response?.data));
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return axios(originalRequest);
        }
      } catch (error) {
        window.alert("Failed to get refresh token.");
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export const register = async (user) => {
  return await postApi(`${API_URL}/signup`, user)
    .then(() => {
      window.location.href = "/login";
    })
    .catch(() => {
      window.alert("Sign up failed");
    });
};

export const login = async (request) => {
  return await postApi(`${API_URL}/login`, request)
    .then((response) => {
      if (response?.accessToken) {
        localStorage.setItem("user", JSON.stringify(response));
      }
      return response;
    })
    .then(() => {
      window.location.href = "/";
    })
    .catch(() => {
      window.alert("Wrong email or password");
    });
};

export const logout = () => {
  localStorage.removeItem("user");
  window.location.href = "/login";
};

export const getApi = async (url, conf = {}) => {
  conf = {
    ...conf,
    headers: addAuthHeader(),
  };
  return await axios
    .get(url, conf)
    .then((response) => {
      return response?.data;
    })
    .catch((error) => {
      throw error;
    });
};

export const postApi = async (url, data = {}, conf = {}) => {
  conf = {
    ...conf,
    headers: addAuthHeader(),
  };
  return await axios
    .post(url, data, conf)
    .then((response) => {
      return response?.data;
    })
    .catch((error) => {
      throw error;
    });
};

export const updateApi = async (url, data = {}, conf = {}) => {
  conf = {
    ...conf,
    headers: addAuthHeader(),
  };
  return await axios
    .patch(url, data, conf)
    .then((response) => {
      return response?.data;
    })
    .catch((error) => {
      throw error;
    });
};

export const deleteApi = async (url, conf = {}) => {
  conf = {
    ...conf,
    headers: addAuthHeader(),
  };
  return await axios
    .delete(url, conf)
    .then((response) => {
      return response?.data;
    })
    .catch((error) => {
      throw error;
    });
};
