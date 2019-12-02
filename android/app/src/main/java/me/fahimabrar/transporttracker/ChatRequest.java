package me.fahimabrar.transporttracker;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by jonesrandom on 12/26/17.
 *
 * @site www.androidexample.web.id
 * @github @alfianyusufabdullah
 */

public class ChatRequest {

    public static void getChat(final OnChatRequest chatResult, String id) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat/"+id);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ModelChat chat = dataSnapshot.getValue(ModelChat.class);
                chatResult.result(chat);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void postMessage(ModelChat chat, String id){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat/"+id);
        String keyChat = reference.push().getKey();
        reference.child(keyChat).setValue(chat);

    }

    public interface OnChatRequest {
        void result(ModelChat chat);
    }
}
