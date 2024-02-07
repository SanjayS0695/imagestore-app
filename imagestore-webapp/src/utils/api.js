import axios from "axios";

const client = axios.create({
  Accept: "application/json",
  "Content-Type": "application/json",
});

export const getApi = (url, conf = {}) => {
  // client.defaults.headers.common.Authorization =
  //   "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW5qYXkuc3VyZXNoQGltZ2FwcC5jb20iLCJpYXQiOjE3MDcyOTkzNDYsImV4cCI6MTcwNzMwMDc4Nn0.5HC1rYeTTMq1wHnQQ3FPgoqvUy7Z8krPJ9jS_HoItz8";

  conf = {
    ...conf,
    headers: {
      Authorization:
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW5qYXkuc3VyZXNoQGltZ2FwcC5jb20iLCJpYXQiOjE3MDcyOTkzNDYsImV4cCI6MTcwNzMwMDc4Nn0.5HC1rYeTTMq1wHnQQ3FPgoqvUy7Z8krPJ9jS_HoItz8",
    },
  };
  return axios
    .get(url, conf)
    .then((response) => {
      return response?.data;
    })
    .catch((error) => {
      throw error;
    });
};

export const postApi = (url, data = {}, conf = {}) => {
  conf = {
    ...conf,
    headers: {
      Authorization:
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW5qYXkuc3VyZXNoQGltZ2FwcC5jb20iLCJpYXQiOjE3MDczMDAwMjgsImV4cCI6MTcwNzMwMTQ2OH0.4lF2I5rg_C4ROFbSbVhPL-rP5g0wZ2DLr4cGIIeAEFM",
    },
  };
  return axios
    .post(url, data, conf)
    .then((response) => {
      return response?.data;
    })
    .catch((error) => {
      throw error;
    });
};

export const updateApi = (url, data = {}, conf = {}) => {
  conf = {
    ...conf,
    headers: {
      Authorization:
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW5qYXkuc3VyZXNoQGltZ2FwcC5jb20iLCJpYXQiOjE3MDcyOTkzNDYsImV4cCI6MTcwNzMwMDc4Nn0.5HC1rYeTTMq1wHnQQ3FPgoqvUy7Z8krPJ9jS_HoItz8",
    },
  };
  return axios
    .patch(url, data, conf)
    .then((response) => {
      return response?.data;
    })
    .catch((error) => {
      throw error;
    });
};

export const deleteApi = (url, conf = {}) => {
  conf = {
    ...conf,
    headers: {
      Authorization:
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW5qYXkuc3VyZXNoQGltZ2FwcC5jb20iLCJpYXQiOjE3MDcyOTkzNDYsImV4cCI6MTcwNzMwMDc4Nn0.5HC1rYeTTMq1wHnQQ3FPgoqvUy7Z8krPJ9jS_HoItz8",
    },
  };
  return axios
    .delete(url, conf)
    .then((response) => {
      return response?.data;
    })
    .catch((error) => {
      throw error;
    });
};
