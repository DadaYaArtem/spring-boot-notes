package com.groupone.notes;


import com.groupone.users.UserEntity;
import com.groupone.users.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@AllArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NotesService service;
    private final UsersService usersService;


    @GetMapping("/list")
    public ModelAndView mainUserPage(HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        UserEntity byEmail = usersService.findByEmail(email);

        ModelAndView modelAndView = new ModelAndView("note-list");

        List<Notes> notesList = byEmail.getNotesList();
        System.out.println("notesList = " + notesList);
        List<Notes> newNotes = new ArrayList<>();

        for (Notes notes : notesList) {
            String content = new String(Jsoup.parse(notes.getContent()).text());

            if (content.length() < 70) {
                System.out.println("content in <70 = " + content);
            } else {
                content = content.substring(0, 70) + "...";
                System.out.println("content else " + content);
            }

            newNotes.add(new Notes(notes.getId(), notes.getNameNotes(), content, notes.getVisibility()));
        }

        System.out.println("newNotes = " + newNotes);

        modelAndView.addObject("count", notesList.size());
        modelAndView.addObject("listOfNotes", newNotes);

        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createNote() {
        ModelAndView modelAndView = new ModelAndView("note-create");
        modelAndView.addObject("notes", new Notes());
        return modelAndView;
    }

    @PostMapping("/save")
    public void saveNote(Notes note,
                         @RequestParam(name = "access") String access,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        String email = request.getUserPrincipal().getName();
        service.createNote(note.getNameNotes(), note.getContent(), Visibility.valueOf(access), email);

        response.sendRedirect("list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editNote(@PathVariable("id") UUID uuid,
                                 HttpServletRequest request) {
        Notes note = service.getNoteByUuid(uuid);

        if (note.getUser().getEmail().equals(request.getUserPrincipal().getName())) {

            ModelAndView modelAndView = new ModelAndView("note-edit");
            modelAndView.addObject("notes", note);

            return modelAndView;
        } else {
            return new ModelAndView("note-share-error");
        }
    }

    @PostMapping("/edit/{id}/save")
    public void updateNote2(@PathVariable("id") UUID uuid,
                            Notes notes,
                            HttpServletResponse response) throws IOException {
        service.updateNote(uuid, notes.getNameNotes(), notes.getContent(), notes.getVisibility());

        response.sendRedirect("/note/list");

    }


    @GetMapping("/share/{id}")
    public ModelAndView shareNote(@PathVariable("id") UUID uuid,
                                  HttpServletResponse response,
                                  HttpServletRequest request) {

        try {
            Notes note = service.getNoteByUuid(uuid);
            if (note.getVisibility().equals(Visibility.PUBLIC) ||
                    note.getUser().getEmail().equals(request.getUserPrincipal().getName())) {

                ModelAndView modelAndView = new ModelAndView("note-share");
                modelAndView.addObject("getNameNotes", note.getNameNotes());
                modelAndView.addObject("getContent", note.getContent());
                modelAndView.addObject("getId", note.getId());
                return modelAndView;
            } else {
                return new ModelAndView("note-share-error");
            }

        } catch (Exception ex) {
            return new ModelAndView("note-share-error");
        }
    }

    @PostMapping("/delete/{id}")
    public void deleteNote(@PathVariable("id") UUID uuid, HttpServletResponse response) throws IOException {
        service.deleteNoteByUuid(uuid);

        response.sendRedirect("/note/list");
    }
}
