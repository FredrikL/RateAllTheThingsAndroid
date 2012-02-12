package com.tacticalnuclearstrike.rateallthethings.api;

import com.tacticalnuclearstrike.rateallthethings.model.BarCode;
import com.tacticalnuclearstrike.rateallthethings.model.Comment;
import com.tacticalnuclearstrike.rateallthethings.model.User;

import java.util.List;

/**
 * User: Fredrik / 2012-01-08
 */
public interface IService {
    BarCode lookUpBarCode(String format, String code);
    
    String createUser(String email);
    BarCode updateBarCode(BarCode barCode);
    
    Boolean addComment(Comment comment);
    List<Comment> getCommentsForBarCode(long barCodeId);

    BarCode rateBarCode(long barCodeId, int rating);
    
    Boolean testCredentials(String username, String password);

    User getCurrentUser();
}

