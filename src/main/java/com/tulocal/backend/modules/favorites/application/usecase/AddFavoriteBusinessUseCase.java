package com.tulocal.backend.modules.favorites.application.usecase;

import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;
import com.tulocal.backend.modules.User.domain.repository.UserRepository;
import com.tulocal.backend.modules.favorites.api.request.AddFavoriteBusinessRequest;
import com.tulocal.backend.modules.favorites.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddFavoriteBusinessUseCase {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

    public void execute(AddFavoriteBusinessRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException("No existe el usuario indicado");
        }
        if (businessRepository.findById(request.getBusinessId()) == null) {
            throw new IllegalArgumentException("No existe el negocio indicado");
        }
        favoriteRepository.addBusinessFavorite(request.getUserId(), request.getBusinessId(), request.getNotifyOffers(), request.getNotifyNewBranch());
    }
}