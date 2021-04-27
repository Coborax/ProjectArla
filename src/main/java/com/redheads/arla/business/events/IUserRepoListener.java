package com.redheads.arla.business.events;

import com.redheads.arla.business.repo.UserRepo;

public interface IUserRepoListener {

    void userRepoChanged(UserRepo repo);

}
