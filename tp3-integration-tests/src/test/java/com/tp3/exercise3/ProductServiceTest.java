package com.tp3.exercise3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Exercice 3 - ProductService Integration Tests")
class ProductServiceTest {

    @Mock
    private ProductApiClient productApiClient;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productApiClient);
    }

    // ─────────────────────────────────────────────
    // Scénario 1 : Récupération réussie
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Scénario 1 - getProduct doit retourner le produit lors d'un appel réussi")
    void testGetProduct_Success() {
        // Arrange
        String productId = "PROD-001";
        Product expectedProduct = new Product(productId, "Laptop Pro", 1299.99, "Informatique");
        when(productApiClient.getProduct(productId)).thenReturn(expectedProduct);

        // Act
        Product result = productService.getProduct(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Laptop Pro", result.getName());
        assertEquals(1299.99, result.getPrice(), 0.001);
        assertEquals("Informatique", result.getCategory());

        // Vérifier que le client API est appelé avec le bon argument
        verify(productApiClient, times(1)).getProduct(productId);
    }

    @Test
    @DisplayName("Scénario 1 - getProduct doit appeler productApiClient avec le bon productId")
    void testGetProduct_CallsApiClientWithCorrectId() {
        // Arrange
        String productId = "PROD-XYZ";
        when(productApiClient.getProduct(anyString())).thenReturn(null);

        // Act
        productService.getProduct(productId);

        // Assert : uniquement appelé avec "PROD-XYZ"
        verify(productApiClient).getProduct("PROD-XYZ");
        verify(productApiClient, never()).getProduct("PROD-001");
    }

    // ─────────────────────────────────────────────
    // Scénario 2 : Format de données incompatible
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Scénario 2 - getProduct doit propager InvalidDataFormatException si le format est incompatible")
    void testGetProduct_InvalidDataFormat() {
        // Arrange
        String productId = "PROD-BAD";
        when(productApiClient.getProduct(productId))
                .thenThrow(new InvalidDataFormatException(
                        "Format de réponse incompatible : champ 'price' attendu mais reçu 'cout'"));

        // Act & Assert
        InvalidDataFormatException exception = assertThrows(InvalidDataFormatException.class,
                () -> productService.getProduct(productId));

        assertTrue(exception.getMessage().contains("Format de réponse incompatible"));

        // Vérifier que le client API a bien été appelé
        verify(productApiClient, times(1)).getProduct(productId);
    }

    // ─────────────────────────────────────────────
    // Scénario 3 : Échec d'appel API
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Scénario 3 - getProduct doit propager ApiCallException si l'appel API échoue")
    void testGetProduct_ApiCallFailure() {
        // Arrange
        String productId = "PROD-DOWN";
        when(productApiClient.getProduct(productId))
                .thenThrow(new ApiCallException("Service externe indisponible (HTTP 503)"));

        // Act & Assert
        ApiCallException exception = assertThrows(ApiCallException.class,
                () -> productService.getProduct(productId));

        assertEquals("Service externe indisponible (HTTP 503)", exception.getMessage());

        // Vérifier que le client API a bien été appelé
        verify(productApiClient, times(1)).getProduct(productId);
    }

    @Test
    @DisplayName("Scénario 3 - getProduct doit propager ApiCallException en cas de timeout")
    void testGetProduct_Timeout() {
        // Arrange
        String productId = "PROD-SLOW";
        when(productApiClient.getProduct(productId))
                .thenThrow(new ApiCallException("Timeout : le serveur n'a pas répondu dans les délais"));

        // Act & Assert
        ApiCallException exception = assertThrows(ApiCallException.class,
                () -> productService.getProduct(productId));

        assertTrue(exception.getMessage().contains("Timeout"));
        verify(productApiClient, times(1)).getProduct(productId);
    }

    @Test
    @DisplayName("Scénario 3 - getProduct doit propager ApiCallException en cas d'erreur réseau")
    void testGetProduct_NetworkError() {
        // Arrange
        String productId = "PROD-NET";
        when(productApiClient.getProduct(productId))
                .thenThrow(new ApiCallException("Erreur réseau : impossible de joindre le serveur",
                        new java.net.ConnectException("Connection refused")));

        // Act & Assert
        ApiCallException exception = assertThrows(ApiCallException.class,
                () -> productService.getProduct(productId));

        assertNotNull(exception.getCause());
        verify(productApiClient, times(1)).getProduct(productId);
    }
}
