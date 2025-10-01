import axios from "axios";
import { useContext, useMemo } from "react";
import { AuthContext } from "../context/AuthContext";

export const useApi = (baseURL) => {
  const { token } = useContext(AuthContext);

  const api = useMemo(() => {
    const instance = axios.create({
      baseURL,
      headers: { "Content-Type": "application/json" },
    });

    instance.interceptors.request.use(
      (config) => {
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    instance.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response) {
          const data = error.response.data;
          if (typeof data === 'object') {
            return Promise.reject({ ...data, status: error.response.status });
          } else {
            return Promise.reject({ message: data, status: error.response.status });
          }
        }
        return Promise.reject({ message: error.message, status: 0 });
      }  
      // (error) => Promise.reject(error.response?.data || error.message)
    );

    return instance;
  }, [baseURL, token]);

  return api;
};
