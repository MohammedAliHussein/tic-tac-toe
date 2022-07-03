<script>
	import { Link } from "svelte-navigator";
	import {fly} from "svelte/transition";
	import {circOut} from "svelte/easing";
    export let text = "";
	export let page = "/";
	export let delay = 0;

    function getIn(delayIndex) {
        return {
            delay: delayIndex * 100,
            y: 20,
            easing: circOut
        }
    }
</script>

<div class="wrapper" in:fly={getIn(delay)}>
	<Link class="link" to={page} >
		{text}
	</Link>
</div>

<style>
    .wrapper > :global(a) {
		text-decoration: none;
		display: flex;
		justify-content: center;
		align-items: center;
    }
    
    :global(a) {
		outline: none;
		background: none;
		border: 1px solid white;
		cursor: pointer;
		padding: 2px 10px;
		margin-top: 0.5em;
		transition: 0.2s cubic-bezier(0, 0.55, 0.45, 1);
		font-size: 12px;
        margin-bottom: 5px;
		color: white;
		
    }

	:global(a):hover {
		background-color: white;
		color: black;
	} 
</style>