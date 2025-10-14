import axios from "axios";
import { useMemo } from "react";

export const useApi = (baseURL) => {
  const api = useMemo(() => {
    const instance = axios.create({
      baseURL,
      headers: { "Content-Type": "application/json"},
      withCredentials: true,
    });

    instance.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response) {
          const data = error.response.data;
          if (typeof data === "object") {
            return Promise.reject({ ...data, status: error.response.status });
          } else {
            return Promise.reject({ message: data, status: error.response.status });
          }
        }
        return Promise.reject({ message: error.message, status: 0 });
      }
    );

    return instance;
  }, [baseURL]);

  return api;
};
