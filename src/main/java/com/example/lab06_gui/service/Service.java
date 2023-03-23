package com.example.lab06_gui.service;

import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.repository.Repository;
import com.example.lab06_gui.utils.events.ChangeEventType;
import com.example.lab06_gui.utils.events.PrietenieEntityChangeEvent;
import com.example.lab06_gui.utils.events.UtilizatorEntityChangeEvent;
import com.example.lab06_gui.utils.observer.Observable;
import com.example.lab06_gui.utils.observer.Observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Service implements Observable<PrietenieEntityChangeEvent> {
    Repository<Long, Utilizator> repoUtilizator;
    Repository<String, Prietenie> repoPrietenie;

    private List<Observer<PrietenieEntityChangeEvent>> obs = new ArrayList<>();

    public Service(Repository<Long, Utilizator> repoUtilizator, Repository<String, Prietenie> repoPrietenie){
        this.repoUtilizator = repoUtilizator;
        this.repoPrietenie = repoPrietenie;
    }

    public boolean addUser(String nume, String prenume, String email, String parola){
        Long ID = getMaxID() + 1;
        Utilizator user = new Utilizator(ID, nume, prenume, email, parola);//, dataNasterii);
        try {
            repoUtilizator.save(user);
            return true;
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Utilizator findUserEmailParola(String email, String password){return repoUtilizator.findByTwoStrings(email,password);}

    public boolean updateUser(Long ID, String nume, String prenume, String email, String parola){
        Utilizator user = new Utilizator(ID, nume, prenume, email, parola);//, dataNasterii);
        try {
            return repoUtilizator.update(user) == null;
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateFriendship(Long ID1, Long ID2, boolean acceptat){
        Prietenie prietenie = new Prietenie(ID1, ID2, generateFriendsFrom());
        prietenie.setAcceptat(acceptat);
        try {
            Prietenie val = repoPrietenie.update(prietenie);
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.ADD, prietenie));
            return val == null;
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Utilizator findUser(Long ID){
        return repoUtilizator.findOne(ID);
    }
    public Prietenie findFriendship(String ID){
        return repoPrietenie.findOne(ID);
    }

    public boolean addFriendship(Long ID1, Long ID2){
        LocalDateTime friendsFrom = generateFriendsFrom();

        Utilizator u1 = repoUtilizator.findOne(ID1);
        Utilizator u2 = repoUtilizator.findOne(ID2);

        if (u1 == null || u2 == null) {
            throw new Error("Unul din utilizatori nu e valid!");
        }
        Prietenie friendship = new Prietenie(ID1, ID2, friendsFrom);
        friendship.setAcceptat(true);
        try {
            repoPrietenie.save(friendship);
            return true;
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public LocalDateTime generateFriendsFrom(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeString = now.format(formatter);

        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public boolean sendFriendRequest(Long ID1, Long ID2){
        LocalDateTime requestFrom = generateFriendsFrom();

        Utilizator u1 = repoUtilizator.findOne(ID1);
        Utilizator u2 = repoUtilizator.findOne(ID2);

        if (u1 == null || u2 == null) {
            throw new Error("Unul din utilizatori nu e valid!");
        }
        Prietenie friendship = new Prietenie(ID1, ID2, requestFrom);
        friendship.setAcceptat(false);

        try {
            repoPrietenie.save(friendship);
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.ADD, friendship));
            return true;
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());

            if(Objects.equals(e.getMessage(), "ID duplicat!!!")) {
                Prietenie p_aux = new Prietenie(ID1, ID2, null);
                Prietenie p_dejaExistent = findFriendship(p_aux.getID());
                if(Objects.equals(p_dejaExistent.getID2(), ID1))
                    updateFriendship(ID1, ID2, true);
            }
            return false;
        }
    }

    public Long getMaxID(){
        List<Utilizator> all = getListUtilizatori();
        Long maxx= 0L;
        for(Utilizator u : all)
            if(u.getID()>maxx)
                maxx=u.getID();

        return maxx;
    }


    public boolean deleteUser(Long ID){
        return repoUtilizator.delete(ID) != null;
    }

    public boolean deleteFriendship(String ID){
        Prietenie p = repoPrietenie.delete(ID);
        notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.DELETE, p));
        return p != null;
    }


    public Iterable<Utilizator> getAll(){
        return repoUtilizator.findAll();
    }

    public Iterable<Prietenie> getAllPrietenii(){
        return repoPrietenie.findAll();
    }

    public List<Utilizator> getListUtilizatori(){
        return repoUtilizator.getAllAsList();
    }

    public List<Prietenie> getListPrietenie(){
        return repoPrietenie.getAllAsList();
    }

    public List<Utilizator> getFriendsOfUserList(Utilizator userCurent){
        List<Prietenie> allFriends = getListPrietenie();
        List<Utilizator> rezultat = new ArrayList<>();

        for(Prietenie p : allFriends.stream().filter(x->x.isAcceptat()).toList())
        {
            Utilizator u1 = findUser(p.getID1());
            Utilizator u2 = findUser(p.getID2());

            if(Objects.equals(p.getID1(), userCurent.getID()))
                rezultat.add(u2);
            else if(Objects.equals(p.getID2(), userCurent.getID()))
                rezultat.add(u1);
        }
        return rezultat;
    }

    public List<Utilizator> getRequestsOfUserList(Utilizator userCurent){
        List<Prietenie> allFriends = getListPrietenie();
        List<Utilizator> rezultat = new ArrayList<>();

        for(Prietenie p : allFriends.stream().filter(x->!(x.isAcceptat())).toList())
        {
            Utilizator u1 = findUser(p.getID1());
            //Utilizator u2 = findUser(p.getID2());
            u1.setDate(p.getFriendsFrom());
            if(Objects.equals(p.getID2(), userCurent.getID()))
                rezultat.add(u1);
        }
        return rezultat;
    }

    public List<Utilizator> getStrangersOfUserList(Utilizator userCurent) {
        List<Utilizator> allUsers = getListUtilizatori();
        List<Utilizator> allFriendsofUser = getFriendsOfUserList(userCurent);
        List<Utilizator> rezultat = new ArrayList<>();

        for(Utilizator u : allUsers)
        {
            if (!allFriendsofUser.contains(u) && !rezultat.contains(u) && !u.equals(userCurent))
                rezultat.add(u);
        }
        return rezultat;
    }

    public int getNrUtilizatori(){return repoUtilizator.getSize(); }

    private void DFS(int nodCurent, boolean[] vizitat, List<Prietenie> listaPrietenie, List<Utilizator> listaUtilizator) {
        Long id1, id2;
        vizitat[nodCurent] = true;

        for (Prietenie p : listaPrietenie) {
            id1 = p.getID1();
            id2 = p.getID2();
            Utilizator prieten1 = repoUtilizator.findOne(id1);
            Utilizator prieten2 = repoUtilizator.findOne(id2);

            if(Objects.equals(id1, listaUtilizator.get(nodCurent).getID()) && !vizitat[listaUtilizator.indexOf(prieten2)]){
                DFS(listaUtilizator.indexOf(prieten2), vizitat, listaPrietenie, listaUtilizator);
            }
            if(Objects.equals(id2, listaUtilizator.get(nodCurent).getID()) && !vizitat[listaUtilizator.indexOf(prieten1)]){
                DFS(listaUtilizator.indexOf(prieten1), vizitat, listaPrietenie, listaUtilizator);
            }
        }
    }

    private void DFSdistante(int nodCurent, int[] distante, List<Prietenie> listaPrietenie, List<Utilizator> listaUtilizator) {
        Long id1, id2;
        distante[nodCurent] += 1;

        for (Prietenie p : listaPrietenie) {
            id1 = p.getID1();
            id2 = p.getID2();
            Utilizator prieten1 = repoUtilizator.findOne(id1);
            Utilizator prieten2 = repoUtilizator.findOne(id2);

            if(Objects.equals(id1, listaUtilizator.get(nodCurent).getID()) && distante[listaUtilizator.indexOf(prieten2)] == 0){
                distante[listaUtilizator.indexOf(prieten2)] = distante[nodCurent];
                DFSdistante(listaUtilizator.indexOf(prieten2), distante, listaPrietenie, listaUtilizator);
            }
            if(Objects.equals(id2, listaUtilizator.get(nodCurent).getID()) && distante[listaUtilizator.indexOf(prieten1)] == 0){
                distante[listaUtilizator.indexOf(prieten1)] = distante[nodCurent];
                DFSdistante(listaUtilizator.indexOf(prieten1), distante, listaPrietenie, listaUtilizator);
            }
        }
    }

    public int numarComunitati() {
        List<Prietenie> listaPrietenie = getListPrietenie();
        List<Utilizator> listaUtilizator = getListUtilizatori();

        int nr = 0;
        boolean[] vizitat = new boolean[listaUtilizator.size()];
        for (int i = 0; i < listaUtilizator.size(); i++) {
            vizitat[i] = false;
        }

        for (int i = 0; i < listaUtilizator.size(); i++) {
            if (!vizitat[i]) {
                DFS(i, vizitat, listaPrietenie, listaUtilizator);
                nr++;
            }
        }
        return nr;
    }

    public List<Long> ceamaiSociabilaComunitate() {
        //returneaza componenta conexa / comunitatea care are cele mai multe noduri / utilizatori
        List<Prietenie> listaPrietenie = getListPrietenie();
        List<Utilizator> listaUtilizator = getListUtilizatori();

        int size = listaUtilizator.size();
        boolean[] vizitat = new boolean[size];
        boolean[] vizitat_ulterior = new boolean[size];
        List<Long>listFinal=new ArrayList<>();

        int maxim=0;
        for (int i = 0; i < size; i++)
            if (!vizitat[i]) {
                DFS(i, vizitat, listaPrietenie, listaUtilizator);
                int nr=0;
                List<Long>listForNow = new ArrayList<>();
                for(int p = 0; p < size; p++){
                    if(vizitat[p]!=vizitat_ulterior[p]){
                        nr++;
                        listForNow.add(listaUtilizator.get(p).getID());
                        vizitat_ulterior[p]=vizitat[p];
                    }
                    if(nr>maxim){
                        listFinal.clear();
                        listFinal.addAll(listForNow);
                        maxim=nr;
                    }
                }
            }
        return listFinal;
    }

    public List<Long> ceamaiSociabilaComunitate2() {
        Iterable<Prietenie> prietenieIterable = repoPrietenie.findAll();
        List<Prietenie> listaPrietenie = new ArrayList<>();
        prietenieIterable.forEach(listaPrietenie::add);

        Iterable<Utilizator> utilizatorIterable = repoUtilizator.findAll();
        List<Utilizator> listaUtilizator = new ArrayList<>();
        utilizatorIterable.forEach(listaUtilizator::add);

        int size = listaUtilizator.size();
        boolean[] vizitat = new boolean[size];
        boolean[] vizitat_ulterior = new boolean[size];
        List<Long>listFinal=new ArrayList<>();

        int maxfinal = 0, maxlocal;
        for (int i = 0; i < size; i++)
            if (!vizitat[i]) {
                DFS(i, vizitat, listaPrietenie, listaUtilizator);
                int nr=0;
                List<Utilizator>listForNowU = new ArrayList<>(); //lista temporara cu utilizatorii unei componente
                List<Prietenie>listForNowP = new ArrayList<>(); //lista temporara cu prietenii/legaturile intr-o componenta

                for(int p = 0; p < size; p++)
                    if(vizitat[p]!=vizitat_ulterior[p]){
                        nr++;
                        listForNowU.add(listaUtilizator.get(p));
                        vizitat_ulterior[p]=vizitat[p];
                    } //adaug utilizatorii din componenta curenta conexa

                for (Prietenie prietenie : listaPrietenie)
                    if (listForNowU.contains(findUser(prietenie.getID1())) || listForNowU.contains(findUser(prietenie.getID2()))) {
                        listForNowP.add(prietenie);
                    } //adaug muchiile din comp conexa curenta

                maxlocal = 0;
                for(int k = 0; k < listForNowU.size(); k++) {
                    int[] distante = new int[listForNowU.size()];
                    distante[k]=0;
                    DFSdistante(k, distante, listForNowP, listForNowU);
                    for(int j=0; j < listForNowU.size(); j++)
                        if(distante[j] >= maxlocal)
                            maxlocal=distante[j];
                }
                if(maxlocal>maxfinal) {
                    maxfinal = maxlocal;
                    listFinal.clear();
                    for(Utilizator u : listForNowU)
                        listFinal.add(u.getID());
                }
            }
        return listFinal;
    }

    @Override
    public void addObserver(Observer<PrietenieEntityChangeEvent> e) {
        obs.add(e);
    }

    @Override
    public void removeObserver(Observer<PrietenieEntityChangeEvent> e) {
        obs.remove(e);
    }

    @Override
    public void notifyObservers(PrietenieEntityChangeEvent entity) {
        obs.stream().forEach(x->x.update(entity));
    }
}