package com.tp3.exercise3;

/**
 * Interface représentant un client d'API externe pour récupérer les produits.
 */
public interface ProductApiClient {
    /**
     * Récupère un produit depuis l'API externe par son identifiant.
     *
     * @param productId l'identifiant du produit
     * @return le produit correspondant
     * @throws ApiCallException           en cas d'échec de l'appel API
     * @throws InvalidDataFormatException si la réponse de l'API est dans un format incompatible
     */
    Product getProduct(String productId);
}
