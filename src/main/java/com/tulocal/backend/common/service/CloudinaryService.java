package com.tulocal.backend.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg", "image/png", "image/jpg", "image/webp");

    // ─── SUBIR ────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public String upload(MultipartFile file, String folder) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("El archivo supera el tamaño máximo de 5MB");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Tipo de archivo no permitido. Solo JPEG, PNG, JPG y WEBP");
        }

        // 💡 Corrección de la transformación para la SDK de Java usando la clase
        // Transformation
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folder,
                        "resource_type", "image",
                        "transformation", new Transformation<>()
                                .width(800)
                                .height(800)
                                .crop("limit")
                                .quality("auto")
                                .fetchFormat("auto")));

        return (String) uploadResult.get("secure_url");
    }

    // ─── ELIMINAR ─────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public boolean delete(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return true; // no es de Cloudinary, nada que borrar
        }

        String publicId = extractPublicId(imageUrl);
        if (publicId == null)
            return false;

        try {
            // Parametrizado con <String, Object> para quitar los warnings del editor
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String status = (String) result.get("result");
            return "ok".equals(status) || "not found".equals(status);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar imagen: " + e.getMessage());
        }
    }

    // ─── REEMPLAZAR (sube nueva + borra vieja) ────────────────────────────────

    public String replace(String oldUrl, MultipartFile newFile, String folder) throws Exception {
        String newUrl = upload(newFile, folder);
        if (oldUrl != null) {
            delete(oldUrl);
        }
        return newUrl;
    }

    // ─── PRIVADO ──────────────────────────────────────────────────────────────

    private String extractPublicId(String url) {
        Pattern pattern = Pattern.compile("/upload/(?:v\\d+/)?(.+)\\.[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }
}