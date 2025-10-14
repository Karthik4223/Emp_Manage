import { useState, useEffect } from "react";

const API_KEY = "ME95TTJOZERmVG9nb3hKNFNtcEJIWHRNMmNqNzdqVFRNclFnTHhRNw==";
const BASE_URL = "https://api.countrystatecity.in/v1";

export const useLocationService = () => {
  const [countryMap, setCountryMap] = useState({});
  const [stateMap, setStateMap] = useState({});
  const [cityMap, setCityMap] = useState({});

  const fetchCountries = async () => {
    const response = await fetch(`${BASE_URL}/countries`, {
      headers: { "X-CSCAPI-KEY": API_KEY },
    });
    const countries = await response.json();
    const countryObj = {};
    countries.forEach((c) => (countryObj[c.iso2] = c.name));
    setCountryMap(countryObj);
    return countryObj;
  };

  const fetchStates = async (countryCode = "IN") => {
    const response = await fetch(`${BASE_URL}/countries/${countryCode}/states`, {
      headers: { "X-CSCAPI-KEY": API_KEY },
    });
    const states = await response.json();
    const stateObj = {};
    states.forEach((s) => (stateObj[s.iso2] = s.name));
    setStateMap(stateObj);
    return stateObj;
  };

  const fetchCities = async (countryCode = "IN", stateCode = "KA") => {
    const response = await fetch(`${BASE_URL}/countries/${countryCode}/states/${stateCode}/cities`, {
      headers: { "X-CSCAPI-KEY": API_KEY },
    });
    const cities = await response.json();
    const cityObj = {};
    cities.forEach((c) => (cityObj[c.name.toUpperCase()] = c.name));
    setCityMap(cityObj);
    return cityObj;
  };

  useEffect(() => {
    (async () => {
      try {
        await fetchCountries();
        await fetchStates();
        await fetchCities();
      } catch (error) {
        console.error("Error fetching location data:", error);
      }
    })();
  }, []);

  return {
    countryMap,
    stateMap,
    cityMap,
    fetchCountries,
    fetchStates,
    fetchCities,
  };
};
