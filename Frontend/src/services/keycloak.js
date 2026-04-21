import Keycloak from "keycloak-js";

const keycloakConfig = {
    url: "http://localhost:8081",
    realm: "TravelAgency",
    clientId: "travel-frontend-client",
};

const keycloak = new Keycloak(keycloakConfig);

export default keycloak;