<script>
  import { Router, Link, Route } from "svelte-navigator";
  import Index from "./pages/Index.svelte";
  import Join from "./pages/Join.svelte";
  import Create from "./pages/Create.svelte";
  import NoSupport from "./components/NoSupport.svelte";

  let minWidth = 641;
  let clientWidth = window.innerWidth;

</script>

<main>
  <Router>
    {#if clientWidth > minWidth}
      <Route path="/">
        <Index />
      </Route>
      <Route path="/join">
        <Join />
      </Route>
      <Route path="/create">
        <Create />
      </Route>
    {:else}
      <NoSupport /> 
    {/if}
  </Router>
</main>

<svelte:window bind:innerWidth={clientWidth} />

<style>
  main {
    width: 100%;
    height: 100vh;
    background-color: rgb(12, 12, 12);
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
