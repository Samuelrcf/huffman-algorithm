import axios, { type AxiosInstance } from "axios";

const apiClient: AxiosInstance = axios.create({
  baseURL: "http://localhost:8082",
  headers: {
    "Content-type": "application/json",
  },
});

export default apiClient;