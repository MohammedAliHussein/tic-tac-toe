<script>
  import { Router, Link, Route } from "svelte-navigator";
  import Index from "./pages/Index.svelte";
  import Join from "./pages/Join.svelte";
  import Create from "./pages/Create.svelte";
  import Game from "./pages/Game.svelte";
  import NoSupport from "./components/NoSupport.svelte";

  let minWidth = 641;
  let clientWidth = window.innerWidth;
  let connectionUrl = "";
  let displayName = "";

  function handleConnectionUrl(event) {
    connectionUrl = (event.detail).url;
    displayName = (event.detail).displayName;
  }
</script>

<main>
  <Router>
    {#if clientWidth > minWidth}
      <Route path="/">
        <Index />
      </Route>
      <Route path="/join">
        <Join on:connectionUrl={handleConnectionUrl}/>
      </Route>
      <Route path="/create">
        <Create on:connectionUrl={handleConnectionUrl}/>
      </Route>
      <Route path="/game">
        <Game connectionUrl={connectionUrl} displayName={displayName}/>
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
    text-align: center;
  }
</style>
