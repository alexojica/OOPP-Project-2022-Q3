package client.avatar;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public interface AvatarGenerator {

    void setNameAndAvatarImage(TextField name, ImageView avatarImage);

    void setAvatarImage();

    void incrementSeed();

    void decrementSeed();

    void renameFile();

    void updateImage();

    void updateAvatar();

    void initAvatar();

    void setAvatarOnClient();
}
