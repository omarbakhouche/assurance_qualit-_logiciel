package com.tp3.exercise3;

public class ProductService {

    private final ProductApiClient productApiClient;

    public ProductService(ProductApiClient productApiClient) {
        this.productApiClient = productApiClient;
    }

    /**
     * Récupère un produit par son identifiant via le client API.
     *
     * @param productId l'identifiant du produit
     * @return le produit récupéré
     * @throws ApiCallException           si l'appel API échoue
     * @throws InvalidDataFormatException si le format de la réponse est incompatible
     */
    public Product getProduct(String productId) {
        return productApiClient.getProduct(productId);
    }
}
