<script>
  import Index from "./pages/Index.svelte";
  import NoSupport from "./components/NoSupport.svelte";

  let minWidth = 641;
  let clientWidth = window.innerWidth;

</script>

<main>
  {#if clientWidth > minWidth}
    <Index />
  {:else}
    <NoSupport /> 
  {/if}
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
