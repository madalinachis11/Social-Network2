package com.example.lab06_gui.domain;

import java.util.List;
import java.util.*;

/**
 * Retea este o clasa ce lucreaza cu legaturile si comunitatile de utilizatori
 * mat - matrix with integer values
 * size - integer(size of matrix)
 * ind - Set that contains int keys
 */
public class Retea {
    private Integer[][] mat;
    private Integer size;
    private Set<Long> ind;

    /**
     * create a matrix of friends connections
     * @param size of matrix, number of users
     */

    //Această clasă Retea este utilizată pentru a reprezenta rețele de utilizatori
    // și prietenii într-o matrice. Matricea este construită în modul următor:

    //pentru fiecare pereche de utilizatori care sunt prieteni,
    // elementele corespunzătoare din matrice sunt setate la 1.
    //pentru fiecare altă pereche de utilizatori, elementele corespunzătoare din matrice sunt setate la 0.
    public Retea(int size) {
        this.ind = new HashSet<>();
        this.mat = new Integer[size][size];
        this.size = size;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.mat[i][j] = 0;
    }

    /**
     * adauga prietenii in retea
     * @param list-list of Friendships
     * Metoda addPrietenii primește ca parametru o listă de obiecte de tip Prietenie
     * și setează valorile matricei de adiacență mat pe pozițiile corespunzătoare utilizatorilor
     * din prietenie pe 1, astfel încât mat[i][j] și mat[j][i] sunt setate pe 1, indicând faptul
     * că cei doi utilizatori sunt prieteni. De exemplu, dacă o prietenie are ID-urile 2 și 3,
     * mat[1][2] și mat[2][1] vor fi setate pe 1.
     */
    public void addPrietenii(Iterable<Prietenie> list) {
        list.forEach(f ->
                this.mat[(int) (f.getID1() - 1)][(int) (f.getID2() - 1)] = 1);
        list.forEach(f ->
                this.mat[(int) (f.getID2() - 1)][(int) (f.getID1() - 1)] = 1);

    }

    /**
     * add users to the network
     * @param list- list of Users
     */
    public void addUtilizatori(Iterable<Utilizator> list) {
        list.forEach(x -> ind.add(x.getID()-1));
    }

    /**
     * dfs algorithm
     * @param v-integer
     * @param visited-boolean array
     * Algoritmul DFS (depth-first search) este folosit pentru a găsi toate componentele conexe din rețea.
     * Componentele conexe sunt grupurile de utilizatori care sunt conectați între ei prin prietenii comune.
     * Numărul total de componente conexe din rețeaua de utilizatori este numărul de comunități din rețea.
     *  Acesta este un algoritm de parcurgere DFS (Depth-First Search) recursivă a grafului reprezentat
     *  prin matricea de adiacență a rețelei.

     * În funcția DFS, se marchează nodul curent ca fiind vizitat prin setarea valorii corespunzătoare
     * în vectorul visited pe poziția corespunzătoare indexului v, și apoi se parcurg recursiv toate
     * nodurile adiacente pentru care nu a fost încă făcută această marcă. Pentru a verifica dacă există
     * o muchie între nodurile v și i, se verifică valoarea matricei de adiacență mat[v][i].

     *În acest mod, se parcurge întregul graf și se identifică componentele conexe.
     */
    private void DFS(int v, boolean[] visited) {
        visited[v] = true;
        for (int i = 0; i < size; i++)
            if (mat[v][i] == 1 && !visited[i])
                DFS(i, visited);
    }

    /**
     * verificam cate componente conexe avem - adica numarul de comunitati
     * @return nr - integer (rezultatul)
     * numarComunitati(): Această metodă calculează numărul de comunități din rețeaua de utilizatori.
     * Algoritmul DFS (depth-first search) este folosit pentru a găsi toate componentele conexe din rețea.
     * Componentele conexe sunt grupurile de utilizatori care sunt conectați între ei prin prietenii comune.
     * Numărul total de componente conexe din rețeaua de utilizatori este numărul de comunități din rețea.
     */
    public int numarComunitati() {
        int nr = 0;
        boolean[] vizitat = new boolean[size];

        for (int i = 0; i < size; i++) {
            if (!vizitat[i] && ind.contains(i)) {
                DFS(i, vizitat);
                nr++;
            }
        }
        return nr;
    }

    /**
     * find the biggest Component
     * @return list of Integer(the result)
     * biggestComponent(): Această metodă găsește cea mai mare componentă conexă din rețeaua de utilizatori.
     * Această componentă conține grupul de utilizatori cu cel mai mare număr de prieteni.
     * Pentru a găsi cea mai mare componentă conexă, această metodă parcurge toate componentele
     * conexe din rețeaua de utilizatori utilizând algoritmul DFS și determină componenta
     * cu cel mai mare număr de utilizatori.
     */
    public List<Integer> biggestComponent() {
        boolean[] visited = new boolean[size];
        boolean[] viz=new boolean[size];
        List<Integer>listFinal=new ArrayList<>();
        int maxim=0;
        for (int i = 0; i < size; i++)
            if (!visited[i] && ind.contains((int) i)) {
                DFS(i, visited);
                int nr=0;
                List<Integer>listForNow=new ArrayList<>();
                for(int p=0;p<size;p++){
                    if(visited[p]!=viz[p]){
                        nr++;
                        listForNow.add((int) p + 1);
                        viz[p]=visited[p];
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
}