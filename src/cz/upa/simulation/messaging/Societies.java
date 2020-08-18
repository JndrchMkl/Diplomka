package cz.upa.simulation.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class Societies  {
    List<PostOffice> postOffices;

    public Societies() {
        this.postOffices = new ArrayList<>();
    }

    public PostOffice join() {
        if (postOffices.size() != 0) {
            int index = new SplittableRandom().nextInt(0, postOffices.size());
            return postOffices.get(index);
        } else {
            return found();
        }
    }

    public PostOffice found() {
        System.out.println("FUNDED");
        PostOffice post = new PostOffice();
        postOffices.add(post);

        return post;
    }

    public List<PostOffice> getPostOffices() {
        return postOffices;
    }


}
