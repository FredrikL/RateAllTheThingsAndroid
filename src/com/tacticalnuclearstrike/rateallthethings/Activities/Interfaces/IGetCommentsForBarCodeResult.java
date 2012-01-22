package com.tacticalnuclearstrike.rateallthethings.Activities.Interfaces;

import com.tacticalnuclearstrike.rateallthethings.model.Comment;

import java.util.List;

public interface IGetCommentsForBarCodeResult {
    void ReturnedComments(List<Comment> comments);
}
