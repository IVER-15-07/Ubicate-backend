package com.tulocal.backend.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map uploadImage(MultipartFile file, String folderName) throws IOException {
        // Configuramos opciones básicas como el nombre de la carpeta en tu dashboard
        Map options = ObjectUtils.asMap(
                "folder", "tulocal/" + folderName,
                "resource_type", "image"
        );
        
        // Sube el archivo y nos devuelve un mapa con toda la información técnica
        return cloudinary.uploader().upload(file.getBytes(), options);
    }
    
}
